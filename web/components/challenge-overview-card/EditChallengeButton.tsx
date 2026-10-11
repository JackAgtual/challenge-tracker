import { Button } from "@/components/ui/button";
import Link from "next/link";

type EditChallengeButtonProps = {
  challengeId: number;
};

export default function EditChallengeButton({
  challengeId,
}: EditChallengeButtonProps) {
  return (
    <Button
      render={<Link href={`/challenges/${challengeId}/edit`} />}
      nativeButton={false}
    >
      Edit challenge
    </Button>
  );
}
