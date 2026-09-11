import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import GoalTable from "./components/GoalTable";
import ParticipantStatus from "./components/ParticipantStatus";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: number }>;
}) {
  await getValidSession();
  const { challengeId } = await params;

  const [challengeDetailRes, goalTableRes] = await Promise.all([
    client.GET("/challenges/{challengeId}", {
      params: { path: { challengeId } },
    }),
    client.GET("/challenges/{challengeId}/goals/completions", {
      params: { path: { challengeId } },
    }),
  ]);

  if (challengeDetailRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  if (goalTableRes.error) {
    throw new Error(`Could not find challenge with id=${challengeId}`);
  }

  const { challenge, participants } = challengeDetailRes.data;
  const { goalCompletionRowResponses, goalDefinitionResponse } =
    goalTableRes.data;

  return (
    <>
      <h1>{challenge.name}</h1>
      {challenge.durationDays && <p>{challenge.durationDays} days</p>}
      <p>Challenge status: {challenge.status}</p>
      {challenge.starDate && <p>Start date: {challenge.starDate}</p>}
      <ParticipantStatus participants={participants} />
      <h2>Your goals</h2>
      <GoalTable
        challengeId={challengeId}
        goalDefinitions={goalDefinitionResponse}
        rows={goalCompletionRowResponses}
      />
    </>
  );
}
