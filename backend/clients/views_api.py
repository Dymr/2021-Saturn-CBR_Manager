# Create your api views here.
from rest_framework import viewsets

from clients.models import Client
from clients.serializer import ClientSerializer


class ClientViewSet(viewsets.ModelViewSet):
    serializer_class = ClientSerializer

    queryset = Client.objects.all()

    def get_queryset(self):
        queryset = self.queryset

        order_by = self.request.query_params.get("ordering", None)

        result_limit = self.request.query_params.get("limit", None)

        if order_by is not None:
            queryset = queryset.order_by(order_by)

        if result_limit is not None:
            queryset = queryset[:int(result_limit)]

        return queryset
