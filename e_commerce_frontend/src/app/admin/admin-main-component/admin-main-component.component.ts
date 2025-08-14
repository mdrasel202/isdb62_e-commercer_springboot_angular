import { Component } from '@angular/core';
import { AdminMainLayoutComponent } from "../admin-main-layout/admin-main-layout.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-main-component',
  imports: [AdminMainLayoutComponent, RouterOutlet],
  templateUrl: './admin-main-component.component.html',
  styleUrl: './admin-main-component.component.css'
})
export class AdminMainComponentComponent {

}
