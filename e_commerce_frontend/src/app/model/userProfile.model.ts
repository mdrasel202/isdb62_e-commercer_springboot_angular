export interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  profileImageUrl?: string;
  birthDate?: Date;
  gender?: string;
  roles?: string[];
}