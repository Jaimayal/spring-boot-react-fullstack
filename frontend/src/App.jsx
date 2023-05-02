import CustomerCard from "./components/CustomerCard";
import Sidebar from "./components/Sidebar";
import { Wrap, WrapItem } from '@chakra-ui/react'

function App() {
    return (
        <Sidebar>
            <Wrap spacingX='3vw' justify='center'>
                <WrapItem>
                    <CustomerCard></CustomerCard>
                </WrapItem>
                <WrapItem>
                    <CustomerCard></CustomerCard>
                </WrapItem>
                <WrapItem>
                    <CustomerCard></CustomerCard>
                </WrapItem>
            </Wrap>
        </Sidebar>
    )
}

export default App
