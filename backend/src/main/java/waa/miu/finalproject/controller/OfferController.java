package waa.miu.finalproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import waa.miu.finalproject.entity.Offer;
import waa.miu.finalproject.entity.dto.input.InputOfferDto;
import waa.miu.finalproject.service.OfferService;

import java.util.List;

@RestController
@RequestMapping("api/v1/offers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping()
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable("id") long offerId) {
        return ResponseEntity.ok(offerService.findById(offerId));
    }

    @PostMapping
    public void createOffer(@RequestBody InputOfferDto inputOffer) {
        offerService.save(inputOffer);
    }
}
