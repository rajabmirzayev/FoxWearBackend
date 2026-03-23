package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.request.CreateAddressRequest;
import com.foxwear.authservice.dto.response.CreateAddressResponse;
import com.foxwear.authservice.entity.Address;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.AddressMapper;
import com.foxwear.authservice.repository.AddressRepository;
import com.foxwear.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    public CreateAddressResponse createAddress(
            CreateAddressRequest request, Long userId
    ) {
        log.info("Creating new address for user ID: {}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        Address address = addressMapper.toEntity(request);

        address.setUser(user);

        var addresses = user.getAddresses();

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

        addressRepository.save(address);
        log.info("Successfully saved address with ID: {} for user: {}", address.getId(), userId);

        return addressMapper.toCreateResponse(address);
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
}
