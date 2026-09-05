import LoginButton from "@/components/LoginButton";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export default function Page() {
  return (
    <>
      <h1>Dashboard</h1>
      <Link href="/challenges/create">
        <Button className="cursor-pointer">Create Challenge</Button>
      </Link>
    </>
  );
}
