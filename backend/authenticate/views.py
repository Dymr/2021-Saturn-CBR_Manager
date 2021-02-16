from django.contrib.auth.models import User
from django.shortcuts import render

# Create your views here.
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token

from users.serializers import UserSerializer


class CustomObtainToken(ObtainAuthToken):

    def post(self, request, *args, **kwargs):
        response = super(CustomObtainToken, self).post(request, *args, **kwargs)

        # attach user to response
        token = response.data["token"]
        user = Token.objects.get(key=token).user

        response.data["user"] = UserSerializer(user).data

        return response
