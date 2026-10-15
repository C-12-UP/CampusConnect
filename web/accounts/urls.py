from django.urls import path
from . import views

urlpatterns = [
    path('login/', views.VividaLoginView.as_view(), name='login'),
]