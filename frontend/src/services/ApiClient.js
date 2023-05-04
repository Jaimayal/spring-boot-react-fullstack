import axios from "axios"

export async function getCustomers() {
    return await axios.get(`http://localhost:8081/api/v1/customers`)
}

export async function saveCustomer(customer) {
    return await axios.post(`http://localhost:8081/api/v1/customers`, customer)
}

export async function deleteCustomer(customerId) {
    return await axios.delete(
        `http://localhost:8081/api/v1/customers/${customerId}`
    )
}
