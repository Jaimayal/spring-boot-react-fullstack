import axios from "axios"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

export async function getCustomers() {
    return await axios.get(`${BASE_URL}/api/v1/customers`)
}

export async function saveCustomer(customer) {
    return await axios.post(`${BASE_URL}/api/v1/customers`, customer)
}

export async function deleteCustomer(customerId) {
    return await axios.delete(`${BASE_URL}/api/v1/customers/${customerId}`)
}

export async function updateCustomer(customerId, updatedCustomer) {
    return await axios.put(
        `${BASE_URL}/api/v1/customers/${customerId}`,
        updatedCustomer
    )
}
