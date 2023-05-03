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
} from '@chakra-ui/react'

export const DrawerForm = () => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button onClick={onOpen}>Open</Button>
            <Drawer isOpen={isOpen} onClose={onClose}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <form
                            id="my-form"
                            onSubmit={(e) => {
                                e.preventDefault()
                                console.log('submitted')
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
