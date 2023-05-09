import axios from "axios"

const BASE_URL = import.meta.env.VITE_API_BASE_URL
const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access-token")}`,
    },
})
export async function getCustomers() {
    return await axios.get(`${BASE_URL}/api/v1/customers`, getAuthConfig())
}

export async function saveCustomer(customer) {
    return await axios.post(`${BASE_URL}/api/v1/customers`, customer)
}

export async function deleteCustomer(customerId) {
    return await axios.delete(
        `${BASE_URL}/api/v1/customers/${customerId}`,
        getAuthConfig()
    )
}

export async function updateCustomer(customerId, updatedCustomer) {
    return await axios.put(
        `${BASE_URL}/api/v1/customers/${customerId}`,
        updatedCustomer,
        getAuthConfig()
    )
}
