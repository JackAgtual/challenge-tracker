import { components } from "@/types/api";
import Link from "next/link";
import { User } from "../User";
import { RemoveParticipantButton } from "./RemoveParticipantButton";

type Props = {
  participant: components["schemas"]["ParticipantResponse"];
  challengeId: number;
  challengeIsPending: boolean;
  curUserIsOwner?: boolean;
  selected: boolean;
};

export function Participant({
  participant,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
  selected,
}: Props) {
  return (
    <div className="flex justify-between py-2 my-0">
      <Link
        className="flex items-center"
        href={`?participant=${participant.id}`}
      >
        <User user={participant.user} />
      </Link>
      {selected && <p>Selected (TODO: REFACTOR)</p>}
      {challengeIsPending && (
        <div className="flex gap-x-2">
          <div className="flex items-center">
            {participant.ready ? "Ready" : "Not ready"}
          </div>
          {curUserIsOwner && !participant.isChallengeOwner && (
            <RemoveParticipantButton
              challengeId={challengeId}
              participantId={participant.id}
            />
          )}
        </div>
      )}
    </div>
  );
}
