import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    useColorModeValue,
    Tag,
    Button,
    useDisclosure,
    AlertDialogFooter,
    AlertDialogBody,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    AlertDialog,
} from "@chakra-ui/react"
import { useRef } from "react"
import * as ApiClient from "../services/ApiClient.js"
import {
    errorToastNotification,
    successToastNotification,
} from "../services/UiNotificationProvider.js"
import DrawerContainer from "./DrawerContainer.jsx"
import CustomerForm from "./CustomerForm.jsx"

export default function CustomerCard({
    id,
    name,
    email,
    age,
    gender,
    fetchCustomers,
}) {
    const pictureGender = gender === "male" ? "men" : "women"
    const {
        isOpen: isDeleteDialogOpen,
        onOpen: onOpenDeleteDialog,
        onClose: onCloseDeleteDialog,
    } = useDisclosure()
    const cancelRef = useRef()
    const {
        isOpen: isDrawerOpen,
        onOpen: onDrawerOpen,
        onClose: onDrawerClose,
    } = useDisclosure()

    function deleteCustomer(customerId) {
        ApiClient.deleteCustomer(customerId)
            .then(() => {
                fetchCustomers()
                successToastNotification(
                    "Success",
                    "Customer deleted successfully"
                )
            })
            .catch((error) => {
                console.log(error)
                errorToastNotification(error.code, error.response.data.message)
            })
            .finally(() => {
                onCloseDeleteDialog()
            })
    }

    function onUpdateFormSubmit(values, { setSubmitting }) {
        setSubmitting(true)
        ApiClient.updateCustomer(id, values)
            .then(() => {
                successToastNotification(
                    "Success",
                    "Customer updated successfully"
                )
                fetchCustomers()
                onDrawerClose()
            })
            .catch((error) =>
                errorToastNotification(error.code, error.response.data.message)
            )
            .finally(() => setSubmitting(false))
    }

    return (
        <Center py={6}>
            <Box
                maxW={"300px"}
                w={"full"}
                bg={useColorModeValue("white", "gray.800")}
                boxShadow={"2xl"}
                rounded={"md"}
                overflow={"hidden"}
            >
                <Image
                    h={"120px"}
                    w={"full"}
                    src={
                        "https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
                    }
                    objectFit={"cover"}
                />
                <Flex justify={"center"} mt={-12}>
                    <Avatar
                        size={"xl"}
                        src={`https://randomuser.me/api/portraits/${pictureGender}/${id}.jpg`}
                        alt={"Author"}
                        css={{
                            border: "2px solid white",
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={2} align={"center"} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading
                            fontSize={"2xl"}
                            fontWeight={500}
                            fontFamily={"body"}
                        >
                            {name}
                        </Heading>
                        <Text color={"gray.500"}>{email}</Text>
                        <Text color={"gray.500"}>
                            Age {age} | {gender}
                        </Text>
                    </Stack>
                    <Stack spacing={2} align={"center"} mb={5}>
                        <Button
                            colorScheme={"red"}
                            variant={"solid"}
                            rounded={"full"}
                            width={"full"}
                            onClick={onOpenDeleteDialog}
                        >
                            Delete
                        </Button>
                        <AlertDialog
                            isOpen={isDeleteDialogOpen}
                            leastDestructiveRef={cancelRef}
                            onClose={onCloseDeleteDialog}
                        >
                            <AlertDialogOverlay>
                                <AlertDialogContent>
                                    <AlertDialogHeader
                                        fontSize="lg"
                                        fontWeight="bold"
                                    >
                                        Delete Customer
                                    </AlertDialogHeader>

                                    <AlertDialogBody>
                                        Are you sure? You can't undo this action
                                        afterwards.
                                    </AlertDialogBody>

                                    <AlertDialogFooter>
                                        <Button
                                            ref={cancelRef}
                                            onClick={onCloseDeleteDialog}
                                        >
                                            Cancel
                                        </Button>
                                        <Button
                                            colorScheme="red"
                                            onClick={() => deleteCustomer(id)}
                                            ml={3}
                                        >
                                            Delete
                                        </Button>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialogOverlay>
                        </AlertDialog>
                        <Button
                            colorScheme={"yellow"}
                            variant={"solid"}
                            rounded={"full"}
                            width={"full"}
                            onClick={onDrawerOpen}
                        >
                            Update
                        </Button>
                        <DrawerContainer
                            header={`Updating ${name}`}
                            isOpen={isDrawerOpen}
                            onClose={onDrawerClose}
                        >
                            <CustomerForm
                                initialValues={{
                                    name,
                                    email,
                                    age,
                                    gender,
                                }}
                                onFormSubmit={onUpdateFormSubmit}
                            />
                            <p>H</p>
                        </DrawerContainer>
                    </Stack>
                </Box>
            </Box>
        </Center>
    )
}
