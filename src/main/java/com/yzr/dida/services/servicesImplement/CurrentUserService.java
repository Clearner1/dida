package com.yzr.dida.services.servicesImplement;

import com.yzr.dida.services.ICurrentUserService;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService implements ICurrentUserService {
    // Placeholder: integrate with real auth later
    public String currentUserId() {
        return "user-1";
    }
}

