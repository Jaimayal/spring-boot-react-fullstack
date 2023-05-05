import CustomerCard from "./components/CustomerCard"
import Sidebar from "./components/Sidebar"
import {
    Wrap,
    WrapItem,
    Spinner,
    Text,
    useDisclosure,
    Button,
} from "@chakra-ui/react"
import * as ApiClient from "./services/ApiClient.js"
import { useEffect, useRef, useState } from "react"
import DrawerContainer from "./components/DrawerContainer.jsx"
import {
    errorToastNotification,
    successToastNotification,
} from "./services/UiNotificationProvider.js"
import CustomerForm from "./components/CustomerForm.jsx"
import ToggleDrawerButton from "./components/shared/ToggleDrawerButton.jsx"

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

    function handleSubmit(
        values,
        setSubmitting,
        submitFunction,
        successMessage
    ) {
        setSubmitting(true)
        submitFunction(values)
            .then((_) => {
                successToastNotification("Success", successMessage)
                fetchCustomers()
                onClose()
            })
            .catch((error) =>
                errorToastNotification(error.code, error.response.data.message)
            )
            .finally(() => setSubmitting(false))
    }

    function onCreateFormSubmit(values, { setSubmitting }) {
        handleSubmit(
            values,
            setSubmitting,
            ApiClient.saveCustomer,
            "Customer created successfully"
        )
    }

    if (isLoading) {
        return (
            <Sidebar>
                <DrawerContainer>
                    <CustomerForm
                        initialValues={{
                            name: "",
                            email: "",
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
                leftIcon={"+"}
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
                        age: "",
                        gender: "",
                    }}
                    onFormSubmit={onCreateFormSubmit}
                />
            </DrawerContainer>
            <Wrap spacingX="3vw" justify="center">
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CustomerCard
                            handleSubmit={handleSubmit}
                            id={customer.id}
                            age={customer.age}
                            email={customer.email}
                            gender={customer.gender}
                            name={customer.name}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </Sidebar>
    )
}

export default App
