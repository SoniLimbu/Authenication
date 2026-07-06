import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080",
});

// Add a request interceptor to automatically attach the token
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// --- Auth Endpoints ---
export const login = (data) => API.post("/api/auth/login", data);
export const register = (data) => API.post("/api/auth/register", data);

// --- Admin Endpoints ---
// Assuming standard REST paths based on your Spring Boot controllers
export const getAdminStats = () => API.get("/api/admin/stats");
export const getUsers = () => API.get("/api/admin/users");
export const updateUserRole = (id, role) => API.put(`/api/admin/users/${id}/role`, { role });
export const toggleUserStatus = (id) => API.put(`/api/admin/users/${id}/toggle-status`);
export const deleteUser = (id) => API.delete(`/api/admin/users/${id}`);
export const createStaff = (data) => API.post("/api/admin/staff", data);

// --- Staff Endpoints ---
export const getStaffProfile = () => API.get("/api/staff/profile");

// --- Customer Endpoints ---
export const getCustomerProfile = () => API.get("/api/customer/profile");

export default API;