import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import ChallengeSection from "./components/ChallengeSection";

export default async function Page() {
  await getValidSession();
  const { data, error } = await client.GET("/challenges");

  if (error) {
    throw new Error("Could not find your challenges");
  }

  return (
    <div className="space-y-2">
      <ChallengeSection title="In Progress" data={data.inProgress} />
      <ChallengeSection title="Pending" data={data.pending} />
      <ChallengeSection title="Complete" data={data.complete} />
    </div>
  );
}
