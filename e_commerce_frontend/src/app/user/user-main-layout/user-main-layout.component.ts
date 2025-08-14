import { Component } from '@angular/core';
import { AdminMainComponentComponent } from "../../admin/admin-main-component/admin-main-component.component";
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-user-main-layout',
  imports: [AdminMainComponentComponent, RouterOutlet],
  templateUrl: './user-main-layout.component.html',
  styleUrl: './user-main-layout.component.css'
})
export class UserMainLayoutComponent {

}
