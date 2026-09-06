import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

  const { data: challenge, error } = await client.GET(
    "/challenges/{challengeId}",
    {
      params: { path: { challengeId } },
    }
  );

  if (error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  return (
    <>
      <h1>{challenge.name}</h1>
      {challenge.durationDays && <p>{challenge.durationDays} days</p>}
      <p>Challenge status: {challenge.status}</p>
      {challenge.starDate && <p>Start date: {challenge.starDate}</p>}
    </>
  );
}
