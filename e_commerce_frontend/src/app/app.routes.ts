import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';
import { NavBarComponent } from './home/nav-bar/nav-bar.component';
import { DeshbordComponent } from './admin/deshbord/deshbord.component';
import { UserPasswordChangeComponent } from './user/user-password-change/user-password-change.component';
import { authGuard } from './core/auth.guard';
import { ProductFormComponent } from './admin/product-form/product-form.component';
import { ProductListComponent } from './admin/product-list/product-list.component';
import { ProductDetailsComponent } from './admin/product-details/product-details.component';
import { AdminMainComponentComponent } from './admin/admin-main-component/admin-main-component.component';
import { UserMainLayoutComponent } from './user/user-main-layout/user-main-layout.component';
import { UserProductDetailsComponent } from './user/user-product-details/user-product-details.component';
import { HomeComponent } from './home/home/home.component';
import { ProductComponentComponent } from './home/product-component/product-component.component';
import { CartComponentComponent } from './home/cart-component/cart-component.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
    // {path: 'editProfile', component: EditProfileComponent},
    // { path: 'nav_var', component: NavBarComponent},
    {path : 'home' , component: HomeComponent},
    {path : 'product-component/:id', component: ProductComponentComponent},
    {path : 'cart-component', component: CartComponentComponent},

    //Admin
    {
        path: 'admin', component: AdminMainComponentComponent,
        children: [
            { path: 'admin-deshbord', component: DeshbordComponent},
            { path: 'product-form', component: ProductFormComponent, canActivate: [authGuard]},
            { path: 'product-form/:id/edit', component: ProductFormComponent},
            { path: 'product-list', component: ProductListComponent},
            { path: 'product-detail/:id', component: ProductDetailsComponent},
            // { path: 'products/:id', component: ProductDetailsComponent},
            // { path: 'product-detail', component: ProductDetailsComponent}
        ]
    },

    //User
    {
        path: 'user' , component: UserMainLayoutComponent,
        children: [
             { path: 'user_password_change', component: UserPasswordChangeComponent},
             
        ]
    },
    {path: 'product-details', component: UserProductDetailsComponent}
];
