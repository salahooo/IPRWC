export interface UserProfile {
  id: number;
  username: string;
  email: string;
  fullName: string;
  gender?: string;
  dateOfBirth?: string;
  roles: string[];
}

export interface UpdateProfileRequest {
  fullName: string;
  gender?: string;
  dateOfBirth: string;
  email: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}
