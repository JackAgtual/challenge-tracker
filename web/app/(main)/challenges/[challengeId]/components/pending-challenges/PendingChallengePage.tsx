import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { components } from "@/types/api";
import ParticipantStatus from "../ParticipantStatus";
import CreateGoalForm from "./CreateGoalForm";
import EditChallengeButton from "./EditChallengeButton";
import InviteUser from "./InviteUser";
import Invites from "./Invites";
import ToggleReady from "./ToggleReady";
import GoalDefinitions from "./goal-definitions";
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
    curParticipant.data.id === challengeOwnerParticipant.data.id;

  return (
    <div>
      {curUserIsChallengeOwner && (
        <>
          <EditChallengeButton challengeId={challenge.id} />
          <StartChallengeButton challengeId={challenge.id} />
        </>
      )}
      <ParticipantStatus
        participants={participants}
        challengeId={challenge.id}
        challengeIsPending={true}
        curUserIsOwner={curUserIsChallengeOwner}
      />
      {curUserIsChallengeOwner && <InviteUser challengeId={challenge.id} />}
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
