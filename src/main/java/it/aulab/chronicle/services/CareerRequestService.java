package it.aulab.chronicle.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aulab.chronicle.models.CareerRequest;
import it.aulab.chronicle.models.Role;
import it.aulab.chronicle.models.User;
import it.aulab.chronicle.repositories.CareerRequestRepository;
import it.aulab.chronicle.repositories.RoleRepository;
import it.aulab.chronicle.repositories.UserRepository;

@Service
public class CareerRequestService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    public boolean alreadyExist(User user) {
        return careerRequestRepository.existsByUserIdAndIsChecked(user.getId(), false);
    }

    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setChecked(false);
        careerRequestRepository.save(careerRequest);

        /* Invio Email */
        emailService.sendSimpleEmail("admin@chronicle.com", "Richiesta per Ruolo: " + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di: " + user.getUsername());
    }

    public CareerRequest find(Long id) {
        return careerRequestRepository.findById(id).get();
    }

    public void careerAccept(Long id) {
        
        //recupero la richiesta
        CareerRequest request = careerRequestRepository.findById(id).get();

        //Dalla richiesta estraggo l'utente richiedente ed il ruolo richiesto
        User user = request.getUser();
        Role role = request.getRole();
        
        //Recupero tutti i ruoli che l'utente già possiede ed aggiungo quello nuovo
        List<Role> newRoles = new ArrayList<>();
        newRoles.add(roleRepository.findByName(role.getName()));
        user.setRoles(newRoles);

        //Salvo le modifiche
        userRepository.save(user);
        request.setChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(user.getEmail(), "Ruolo Abilitato", "Ciao, la tua richiesta è stata accetata dalla nostra amministrazione");

    }
}
