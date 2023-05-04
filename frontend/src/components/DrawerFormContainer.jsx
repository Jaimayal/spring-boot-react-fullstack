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
import CreateCustomerForm from "./CreateCustomerForm"

const AddIcon = () => "+"
const CloseIcon = () => "X"

export default function DrawerFormContainer({ fetchCustomers }) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button
                leftIcon={<AddIcon />}
                colorScheme={"teal"}
                variant={"solid"}
                onClick={onOpen}
            >
                Create Customer
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create Customer</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm
                            fetchCustomers={fetchCustomers}
                            onClose={onClose}
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
