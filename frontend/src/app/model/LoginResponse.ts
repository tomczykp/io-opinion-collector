export interface LoginResponse {
  jwt: string;
  refreshToken: string;
  email: string;
  role: string;
}
