import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure,
} from "@chakra-ui/react"
import UpdateCustomerForm from "./UpdateCustomerForm.jsx"

const AddIcon = () => "+"
const CloseIcon = () => "X"

export default function UpdateFormContainer({ fetchCustomers, customer }) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button
                colorScheme={"yellow"}
                variant={"solid"}
                rounded={"full"}
                width={"full"}
                onClick={onOpen}
            >
                Update
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Now updating {customer.name}...</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm
                            fetchCustomers={fetchCustomers}
                            onClose={onClose}
                            customerValues={customer}
                        />
                    </DrawerBody>
                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon />}
                            colorScheme={"red"}
                            variant={"solid"}
                            onClick={onClose}
                        >
                            Cancel
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}
