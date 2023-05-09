import CustomerCard from "./components/dashboard/CustomerCard.jsx"
import Sidebar from "./components/dashboard/Sidebar.jsx"
import {
    Wrap,
    WrapItem,
    Spinner,
    Text,
    useDisclosure,
    Button,
} from "@chakra-ui/react"
import * as ApiClient from "./services/ApiClient.js"
import { useEffect, useState } from "react"
import DrawerContainer from "./components/dashboard/DrawerContainer.jsx"
import {
    errorToastNotification,
    successToastNotification,
} from "./services/UiNotificationProvider.js"
import CustomerForm from "./components/dashboard/CustomerForm.jsx"

function App() {
    const [customers, setCustomers] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const {
        isOpen: isDrawerOpen,
        onOpen: onDrawerOpen,
        onClose: onDrawerClose,
    } = useDisclosure()

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

    function onCreateFormSubmit(values, { setSubmitting }) {
        setSubmitting(true)
        ApiClient.saveCustomer(values)
            .then(() => {
                successToastNotification(
                    "Success",
                    "Customer created successfully"
                )
                fetchCustomers()
                onDrawerClose()
            })
            .catch((error) =>
                errorToastNotification(error.code, error.response.data.message)
            )
            .finally(() => setSubmitting(false))
    }

    if (isLoading) {
        return (
            <Sidebar>
                <DrawerContainer>
                    <CustomerForm
                        initialValues={{
                            name: "",
                            email: "",
                            password: "",
                            age: "",
                            gender: "",
                        }}
                        onFormSubmit={onCreateFormSubmit}
                    />
                </DrawerContainer>
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
                <DrawerContainer>
                    <CustomerForm
                        initialValues={{
                            name: "",
                            email: "",
                            password: "",
                            age: "",
                            gender: "",
                        }}
                        onFormSubmit={onCreateFormSubmit}
                    />
                </DrawerContainer>
                <Text mt={"5vh"}>No customers found</Text>
            </Sidebar>
        )
    }

    return (
        <Sidebar>
            <Button
                colorScheme={"teal"}
                variant={"solid"}
                onClick={onDrawerOpen}
            >
                Create Customer
            </Button>
            <DrawerContainer
                header={"Creating New Customer"}
                isOpen={isDrawerOpen}
                onClose={onDrawerClose}
            >
                <CustomerForm
                    initialValues={{
                        name: "",
                        email: "",
                        password: "",
                        age: "",
                        gender: "",
                    }}
                    onFormSubmit={onCreateFormSubmit}
                />
            </DrawerContainer>
            <Wrap spacingX="3vw" justify="center">
                {customers.map((customer) => (
                    <WrapItem key={customer.id}>
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
