import axios from "axios"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

export async function login(loginRequest) {
    return await axios.post(`${BASE_URL}/api/v1/auth/login`, loginRequest)
}

export async function register(registerRequest) {
    return await axios.post(`${BASE_URL}/api/v1/customers`, registerRequest)
}
