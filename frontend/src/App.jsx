import CustomerCard from './components/CustomerCard'
import Sidebar from './components/Sidebar'
import { Wrap, WrapItem, Spinner } from '@chakra-ui/react'
import * as HttpClient from './services/HttpClient.js'
import { useEffect, useState } from 'react'
import DrawerFormToggleButton from './components/DrawerFormToggleButton'

function App() {
    const [customers, setCustomers] = useState([])
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        setIsLoading(true)
        HttpClient.getCustomers()
            .then((response) => setCustomers(response.data))
            .catch((error) => {
                console.error(error)
            })
            .finally(() => setIsLoading(false))
    }, [])

    if (isLoading) {
        return (
            <Sidebar>
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
                <DrawerFormToggleButton />
                <Wrap spacingX="3vw" justify="center">
                    <WrapItem>
                        <CustomerCard></CustomerCard>
                    </WrapItem>
                </Wrap>
            </Sidebar>
        )
    }

    return (
        <Sidebar>
            <Wrap spacingX="3vw" justify="center">
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CustomerCard {...customer} />
                    </WrapItem>
                ))}
            </Wrap>
        </Sidebar>
    )
}

export default App
