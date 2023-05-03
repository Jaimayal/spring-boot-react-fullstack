import { Button } from '@chakra-ui/react'

const AddIcon = () => '+'

export default function DrawerFormToggleButton() {
    return (
        <>
            <Button
                leftIcon={<AddIcon />}
                colorScheme={'blue'}
                variant={'solid'}
                onClick={() => alert('CREATE NEW CUSTOMER')}
            >
                Create Customer
            </Button>
        </>
    )
}
