package com.example.cbr_manager.ui;

import androidx.lifecycle.SavedStateHandle;

import com.example.cbr_manager.repository.VisitRepository;
import com.example.cbr_manager.service.visit.Visit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class VisitViewModel {
    private final SavedStateHandle savedStateHandle;
    private VisitRepository visitRepository;

    @Inject
    public VisitViewModel(SavedStateHandle savedStateHandle, VisitRepository visitRepository) {
        this.savedStateHandle = savedStateHandle;
        this.visitRepository = visitRepository;
    }

    public Single<Visit> getVisit(int id) {
        return visitRepository.getVisit(id);
    }

    public Observable<Visit> getVisits() {
        return visitRepository.getVisits();
    }

}
