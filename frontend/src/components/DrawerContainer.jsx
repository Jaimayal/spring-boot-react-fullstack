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
import ToggleDrawerButton from "./shared/ToggleDrawerButton"

const AddIcon = () => "+"
const CloseIcon = () => "X"

export default function DrawerContainer({
    header,
    children,
    isOpen,
    onOpen,
    onClose,
}) {
    return (
        <>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>{header}</DrawerHeader>
                    <DrawerBody>{children}</DrawerBody>
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
