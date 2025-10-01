export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  gender?: string;
  dateOfBirth: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  roles: string[];
}
