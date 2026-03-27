package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.request.AddressCreateRequest;
import com.foxwear.authservice.dto.request.AddressDTO;
import com.foxwear.authservice.dto.request.AddressUpdateRequest;
import com.foxwear.authservice.dto.response.AddressCreateResponse;
import com.foxwear.authservice.dto.response.AddressGetAllResponse;
import com.foxwear.authservice.dto.response.AddressGetResponse;
import com.foxwear.authservice.dto.response.AddressUpdateResponse;
import com.foxwear.authservice.entity.Address;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.AddressNotFoundException;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.AddressMapper;
import com.foxwear.authservice.repository.AddressRepository;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.common.exception.UnauthorizedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    /**
     * Creates a new address for a specific user and manages default status logic.
     *
     * @param request The address details
     * @param userId  The ID of the user owning the address
     * @return CreateAddressResponse containing the saved address details
     */
    @Transactional
    public AddressCreateResponse createAddress(
            AddressCreateRequest request, Long userId
    ) {
        log.info("Creating new address for user ID: {}", userId);

        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        Address address = addressMapper.toEntity(request);

        address.setUser(user);

        var addresses = user.getAddresses();

        setAddressDefault(addresses, address, request);

        addressRepository.save(address);
        log.info("Successfully saved address with ID: {} for user: {}", address.getId(), userId);

        return addressMapper.toCreateResponse(address);
    }

    /**
     * Retrieves a specific address by its ID for a given user.
     *
     * @param id     The ID of the address
     * @param userId The ID of the user
     * @return AddressGetResponse containing address details
     */
    @Transactional(readOnly = true)
    public AddressGetResponse getAddressById(Long id, Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }

        Address address = findAddressOrThrow(id);
        if (!address.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized user");
        }

        return addressMapper.toGetResponse(address);
    }

    /**
     * Retrieves all addresses associated with a specific user.
     *
     * @param userId The ID of the user
     * @return List of AddressGetAllResponse
     */
    @Transactional(readOnly = true)
    public List<AddressGetAllResponse> getAllAddresses(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }

        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(addressMapper::toGetAllResponse)
                .toList();
    }

    /**
     * Updates an existing address and manages default status logic.
     *
     * @param request The updated address details
     * @param id      The ID of the address to update
     * @param userId  The ID of the user owning the address
     * @return AddressUpdateResponse containing the updated address details
     */
    @Transactional
    public AddressUpdateResponse updateAddress(AddressUpdateRequest request, Long id, Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }

        log.info("Updating address ID: {} for user ID: {}", id, userId);

        Address address = findAddressOrThrow(id);
        if (!address.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized user");
        }

        address.setTitle(request.getTitle());
        address.setCity(request.getCity());
        address.setRegion(request.getRegion());
        address.setStreet(request.getStreet());
        address.setBlock(request.getBlock());
        address.setFloor(request.getFloor());
        address.setDoorNumber(request.getDoorNumber());
        address.setDoorCode(request.getDoorCode());
        address.setFullAddressText(request.getFullAddressText());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());

        var addresses = address.getUser().getAddresses();
        setAddressDefault(addresses, address, request);

        log.info("Successfully updated address with ID: {} for user: {}", address.getId(), userId);
        return addressMapper.toUpdateResponse(address);
    }

    /**
     * Deletes an existing address after verifying ownership.
     *
     * @param id     The ID of the address to delete
     * @param userId The ID of the user requesting deletion
     */
    @Transactional
    public void deleteAddress(Long id, Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }

        Address address = findAddressOrThrow(id);
        if (!address.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized user");
        }

        if (address.getIsDefault()) {
            address.getUser()
                    .getAddresses()
                    .getFirst()
                    .setIsDefault(true);
        }

        addressRepository.delete(address);
        log.info("Successfully deleted address with ID: {} for user: {}", address.getId(), userId);
    }

    /**
     * Sets the isDefault flag to false for all addresses in the provided list.
     *
     * @param addresses List of user addresses
     */
    private void makeAllAddressesNonDefault(List<Address> addresses) {
        addresses.stream()
                .filter(Address::getIsDefault)
                .forEach(address -> address.setIsDefault(false));
    }

    /**
     * Finds an address by ID or throws an exception if not found.
     *
     * @param id The ID of the address
     * @return The found Address entity
     */
    private Address findAddressOrThrow(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Address not found with ID: {}", id);
                    return new AddressNotFoundException("Address not found");
                });
    }

    /**
     * Logic to determine and set the default status of an address.
     *
     * @param addresses Existing list of user addresses
     * @param address   The address being created or updated
     * @param request   The DTO containing the requested default status
     */
    private void setAddressDefault(List<Address> addresses, Address address, AddressDTO request) {
        if (addresses.isEmpty()) {
            address.setIsDefault(true);
        } else {
            if (request.getIsDefault()) {
                // If a new address has been sent as default, we set the previous default to false
                makeAllAddressesNonDefault(addresses);
                address.setIsDefault(true);
            } else {
                // If default=false is sent, but there is no default address in the system, set it as default
                boolean hasAnyDefault = addresses.stream().anyMatch(Address::getIsDefault);
                if (!hasAnyDefault) {
                    address.setIsDefault(true);
                }
            }
        }
    }
}
