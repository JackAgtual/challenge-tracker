import { Button } from "@/components/ui/button";
import { getValidSession } from "@/lib/auth-utils";
import Link from "next/link";
import Challenges from "./components/Challenges";

export default async function Page() {
  await getValidSession();

  return (
    <>
      <h1>Dashboard</h1>
      <Challenges />
      <Link href="/challenges/create">
        <Button className="cursor-pointer">Create Challenge</Button>
      </Link>
    </>
  );
}
