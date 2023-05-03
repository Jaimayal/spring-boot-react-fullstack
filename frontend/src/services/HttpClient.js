import axios from 'axios'

export async function getCustomers() {
    return await axios.get(`http://localhost:8081/api/v1/customers`)
}