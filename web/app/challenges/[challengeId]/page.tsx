import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import GoalCompletionTable from "./components/GoalCompletionTable";
import ParticipantStatus from "./components/ParticipantStatus";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

  const challengeDetailRes = await client.GET("/challenges/{challengeId}", {
    params: { path: { challengeId } },
  });

  if (challengeDetailRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  const { challenge, participants } = challengeDetailRes.data;
  return (
    <>
      <h1>{challenge.name}</h1>
      {challenge.durationDays && <p>{challenge.durationDays} days</p>}
      <p>Challenge status: {challenge.status}</p>
      {challenge.starDate && <p>Start date: {challenge.starDate}</p>}
      <ParticipantStatus participants={participants} />
      <h2>Your goals</h2>
      {challenge.status === "PENDING" ? (
        <div>TODO: List goal definitions</div>
      ) : (
        <GoalCompletionTable
          challengeId={challengeId}
          challengeInProgress={challenge.status === "IN_PROGRESS"}
        />
      )}
    </>
  );
}
