import { Button } from "@chakra-ui/react"

const getIcon = (icon) => icon

export default function ToggleDrawerButton({ targetDrawerOpen, text, icon }) {
    return (
        <Button
            leftIcon={icon}
            colorScheme={"teal"}
            variant={"solid"}
            onClick={targetDrawerOpen}
        >
            {text}
        </Button>
    )
}
