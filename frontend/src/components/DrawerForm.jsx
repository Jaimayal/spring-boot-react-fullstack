import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    Input,
    useDisclosure,
} from "@chakra-ui/react"

const AddIcon = () => "+"

function DrawerToggleButton({ onOpen }) {
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
        </>
    )
}

export default function DrawerForm() {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <DrawerToggleButton onOpen={onOpen} />
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <form
                            id="my-form"
                            onSubmit={(e) => {
                                e.preventDefault()
                                console.log("submitted")
                            }}
                        >
                            <Input name="nickname" placeholder="Type here..." />
                        </form>
                    </DrawerBody>
                    <DrawerFooter>
                        <Button type="submit" form="my-form">
                            Save
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}
