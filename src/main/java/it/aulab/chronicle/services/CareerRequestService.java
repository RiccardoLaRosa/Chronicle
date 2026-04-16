package it.aulab.chronicle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aulab.chronicle.models.CareerRequest;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.repositories.CareerRequestRepository;

@Service
public class CareerRequestService {
    
    @Autowired
    CareerRequestRepository careerRequestRepository;

    @Autowired
    EmailService emailService;

    public boolean AlreadyExist(User user) {
        return careerRequestRepository.existsByUserId(user.getId());
    }

    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setChecked(false);
        careerRequestRepository.save(careerRequest);

        /* Invio Email */
        emailService.sendSimpleEmail("admin@chronicle.com", "Richiesta per Ruolo: " + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di: " + user.getUsername());
    }
}
