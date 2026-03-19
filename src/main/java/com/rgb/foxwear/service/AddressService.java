package com.rgb.foxwear.service;

import com.rgb.foxwear.dto.request.auth.CreateAddressRequest;
import com.rgb.foxwear.dto.response.auth.CreateAddressResponse;
import com.rgb.foxwear.entity.auth.Address;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.exception.UserNotFoundException;
import com.rgb.foxwear.repository.auth.AddressRepository;
import com.rgb.foxwear.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public CreateAddressResponse createAddress(
            CreateAddressRequest request, Long userId
    ) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        Address address = mapper.map(request, Address.class);

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

        return mapper.map(address, CreateAddressResponse.class);
    }

    private void makeAllAddressesNonDefault(List<Address> addresses) {
        addresses.stream()
                .filter(Address::getIsDefault)
                .forEach(address -> address.setIsDefault(false));
    }
}
