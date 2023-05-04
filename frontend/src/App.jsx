import CustomerCard from "./components/CustomerCard"
import Sidebar from "./components/Sidebar"
import { Wrap, WrapItem, Spinner, Text, useDisclosure } from "@chakra-ui/react"
import * as ApiClient from "./services/ApiClient.js"
import { useEffect, useRef, useState } from "react"
import DrawerFormContainer from "./components/DrawerFormContainer.jsx"

function App() {
    const [customers, setCustomers] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    function fetchCustomers() {
        setIsLoading(true)
        ApiClient.getCustomers()
            .then((response) => setCustomers(response.data))
            .catch((error) => {
                console.error(error)
            })
            .finally(() => setIsLoading(false))
    }

    useEffect(() => fetchCustomers(), [])

    if (isLoading) {
        return (
            <Sidebar>
                <DrawerFormContainer />
                <Spinner
                    thickness="4px"
                    speed="0.65s"
                    emptyColor="gray.200"
                    color="blue.500"
                    size="xl"
                />
            </Sidebar>
        )
    }

    if (customers.length <= 0) {
        return (
            <Sidebar>
                <DrawerFormContainer />
                <Text mt={"5vh"}>No customers found</Text>
            </Sidebar>
        )
    }

    return (
        <Sidebar>
            <DrawerFormContainer fetchCustomers={fetchCustomers} />
            <Wrap spacingX="3vw" justify="center">
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CustomerCard
                            fetchCustomers={fetchCustomers}
                            {...customer}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </Sidebar>
    )
}

export default App
