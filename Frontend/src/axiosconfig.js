import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://192.168.37.18:8080",
});

console.log("Axios base URL:", import.meta.env.VITE_API_BASE_URL); 

export default api;

