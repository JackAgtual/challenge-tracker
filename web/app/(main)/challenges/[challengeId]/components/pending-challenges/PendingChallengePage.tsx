import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { components } from "@/types/api";
import CreateGoalForm from "./CreateGoalForm";
import GoalDefinitions from "./GoalDefinitions";
import Invites from "./Invites";
import ParticipantStatus from "./ParticipantStatus";
import ToggleReady from "./ToggleReady";
import StartChallengeButton from "./start-challenge-button";

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

  const [goalDefinitions, invites, curParticipant, challengeOwnerParticipant] =
    await Promise.all([
      client.GET(
        "/challenges/{challengeId}/participants/{participantId}/goals",
        {
          params: {
            path: {
              challengeId: challenge.id,
              participantId: curParticipantId,
            },
          },
        }
      ),
      client.GET("/challenges/{challengeId}/invites", {
        params: { path: { challengeId: challenge.id } },
      }),
      client.GET("/challenges/{challengeId}/participants/me", {
        params: {
          path: { challengeId: challenge.id },
        },
      }),
      client.GET("/challenges/{challengeId}/participants/owner", {
        params: {
          path: { challengeId: challenge.id },
        },
      }),
    ]);

  if (goalDefinitions.error) {
    throw new Error(`Unable to get goals for challenge ${challenge.id}`);
  }
  if (invites.error) {
    throw new Error(`Unable to get invites for challenge ${challenge.id}`);
  }
  if (curParticipant.error) {
    throw new Error("Could not get your participant info");
  }
  if (challengeOwnerParticipant.error) {
    throw new Error("Could not get challenge owner");
  }

  const curUserIsChallengeOwner =
    curParticipant.data.participantId ===
    challengeOwnerParticipant.data.participantId;

  return (
    <div>
      {curUserIsChallengeOwner && (
        <StartChallengeButton challengeId={challenge.id} />
      )}
      <ParticipantStatus participants={participants} />
      <ToggleReady
        isReady={curParticipant.data.ready}
        challengeId={challenge.id}
      />
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
