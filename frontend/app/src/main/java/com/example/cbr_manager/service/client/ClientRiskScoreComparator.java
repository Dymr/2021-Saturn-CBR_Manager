package com.example.cbr_manager.service.client;

import java.util.Comparator;

public class ClientRiskScoreComparator implements Comparator<Client> {

    private SortOrder sortOrder;

    public ClientRiskScoreComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Client o1, Client o2) {
        if (this.sortOrder == SortOrder.ASCENDING) {
            return o1.getRiskScore() - o2.getRiskScore();
        }
        return o2.getRiskScore() - o1.getRiskScore();

    }

    public enum SortOrder {ASCENDING, DESCENDING}
}
