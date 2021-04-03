package com.example.cbr_manager.ui;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.cbr_manager.repository.VisitRepository;
import com.example.cbr_manager.service.visit.Visit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.Single;

@HiltViewModel
public class VisitViewModel extends ViewModel {
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

    public Single<Visit> createVisit(Visit visit){
        return visitRepository.createVisit(visit);
    }
}
