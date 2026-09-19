import { components } from "@/types/api";
import ParticipantStatus from "./ParticipantStatus";
import GoalDefinitions from "./GoalDefinitions";
import CreateGoalForm from "./CreateGoalForm";
import { client } from "@/lib/api-client";
import Invites from "./Invites";
import { getValidSession } from "@/lib/auth-utils";

type PendingChallengePageProps = {
  challengeDetails: components["schemas"]["ChallengeDetailResponse"];
  curParticipantId: number;
};

export default async function PendingChallengePage({
  challengeDetails,
  curParticipantId,
}: PendingChallengePageProps) {
  await getValidSession();

  const { challenge, participants } = challengeDetails;

  const [goalDefinitions, invites] = await Promise.all([
    client.GET("/challenges/{challengeId}/participants/{participantId}/goals", {
      params: {
        path: { challengeId: challenge.id, participantId: curParticipantId },
      },
    }),
    client.GET("/challenges/{challengeId}/invites", {
      params: { path: { challengeId: challenge.id } },
    }),
  ]);

  if (goalDefinitions.error) {
    throw new Error(`Unable to get goals for challenge ${challenge.id}`);
  }
  if (invites.error) {
    throw new Error(`Unable to get invites for challenge ${challenge.id}`);
  }

  return (
    <div>
      <ParticipantStatus participants={participants} />
      <Invites invites={invites.data} />
      <h2>Your goals</h2>
      <GoalDefinitions
        challengeId={challenge.id}
        goalDefinitions={goalDefinitions.data}
      />
      <CreateGoalForm challengeId={challenge.id} />
    </div>
  );
}
