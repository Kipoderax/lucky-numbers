import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderMenuComponent} from './header-menu/header-menu.component';
import {AccountComponent} from './account/account.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {RouterModule, Routes} from '@angular/router';
import {ZakladyComponent} from './zaklady/zaklady.component';

const routes: Routes = [
  // {path: '', component: HeaderMenuComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'zaklady', component: ZakladyComponent}
  ];

@NgModule({
  declarations: [
    AppComponent,
    HeaderMenuComponent,
    AccountComponent,
    LoginComponent,
    RegisterComponent,
    ZakladyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
