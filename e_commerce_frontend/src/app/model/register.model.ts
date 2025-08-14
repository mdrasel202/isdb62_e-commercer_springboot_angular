export interface Register {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber?: string;
  birthDate?: Date;
  gender?: string;
  doorNo?: string;
  street?: string;
  city?: string;
  district?: string;
  state?: string;
  role: string;
}