import { createStandaloneToast } from "@chakra-ui/react"

const { toast } = createStandaloneToast()

function getDefaultToast(title, description, status) {
    return toast({
        title: title,
        description: description,
        status: status,
        duration: 7000,
        isClosable: true,
    })
}

export function successToastNotification(title, description) {
    return getDefaultToast(title, description, "success")
}

export function errorToastNotification(title, description) {
    return getDefaultToast(title, description, "error")
}