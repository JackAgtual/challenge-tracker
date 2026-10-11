import ParticipantsCard from "@/components/participants-card";
import { client } from "@/lib/api-client";
import { getValidSession } from "@/lib/auth-utils";
import { components } from "@/types/api";
import GoalDefinitionsCard from "./goal-definitions-card";
import InvitesCard from "./invites-card";
import ChallengeOverviewCard from "@/components/challenge-overview-card";

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
      <ChallengeOverviewCard
        challenge={challengeDetails.challenge}
        canEditAndStartChallenge={curUserIsChallengeOwner}
      />
      <ParticipantsCard
        participants={participants}
        challengeId={challenge.id}
        challengeIsPending={true}
        curUserIsOwner={curUserIsChallengeOwner}
        curUserIsReady={curParticipant.data.ready}
      />
      <InvitesCard
        invites={invites.data}
        challengeId={challenge.id}
        curUserIsChallengeOwner={curUserIsChallengeOwner}
      />
      <GoalDefinitionsCard
        challengeId={challenge.id}
        goalDefinitions={goalDefinitions.data}
      />
    </div>
  );
}
