from django.contrib.auth.views import LoginView


class VividaLoginView(LoginView):
    template_name = 'accounts/login.html'
    redirect_authenticated_user = True

    def form_valid(self, form):
        response = super().form_valid(form)
        # If "Keep me signed in" is not ticked, log out when the browser closes
        if not self.request.POST.get('remember_me'):
            self.request.session.set_expiry(0)
        return response