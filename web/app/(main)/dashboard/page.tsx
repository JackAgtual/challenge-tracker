import { getValidSession } from "@/lib/auth-utils";
import Challenges from "./components/Challenges";

export default async function Page() {
  await getValidSession();

  return (
    <>
      <h1>Dashboard</h1>
      <Challenges />
    </>
  );
}
