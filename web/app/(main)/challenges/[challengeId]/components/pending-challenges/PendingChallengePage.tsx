import { components } from "@/types/api";
import ParticipantStatus from "../ParticipantStatus";
import GoalDefinitions from "./GoalDefinitions";
import CreateGoalForm from "./CreateGoalForm";
import { client } from "@/lib/api-client";

type PendingChallengePageProps = {
  challengeDetails: components["schemas"]["ChallengeDetailResponse"];
};

export default async function PendingChallengePage({
  challengeDetails,
}: PendingChallengePageProps) {
  const { challenge, participants } = challengeDetails;

  const { data: goalDefinitions, error: goalDefinitionsError } =
    await client.GET("/challenges/{challengeId}/goals", {
      params: { path: { challengeId: challenge.id } },
    });

  if (goalDefinitionsError) {
    throw new Error(`Unable to get goals for challenge ${challenge.id}`);
  }

  return (
    <div>
      <ParticipantStatus participants={participants} />
      <h2>Your goals</h2>
      <GoalDefinitions
        challengeId={challenge.id}
        goalDefinitions={goalDefinitions}
      />
      <CreateGoalForm challengeId={challenge.id} />
    </div>
  );
}
